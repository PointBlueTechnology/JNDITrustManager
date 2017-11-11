/*
 * Copyright 2017 PointBlue Technology LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package com.pointbluetech.consulting.jndi;
//  jndiSocketFactory.java
//
//  Created by Jerry Combs on 7/7/05.
//




//import com.sun.net.ssl.*;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class JndiSocketFactory extends SSLSocketFactory implements java.lang.Runnable
{
    
    private static SSLSocketFactory factory = null;
    private static JndiSocketFactory default_factory = null;
    private static ClassLoader myClassLoader = null;
    private static final int TIMEOUT = 5000;
    private static final long TIME = 5000;
    private static  SSLContext sslctx;
    
    
    static{
        
        try {
            if("1.4".compareTo(System.getProperty("java.version")) >= 0)
                sslctx = SSLContext.getInstance("TLS");
            else
                sslctx = SSLContext.getInstance("SSLv3");
            TrustManager[] myTrustMgr = new TrustManager[] {
              new DummyTrustManager() 
            };       
            sslctx.init(null, myTrustMgr, null);
            
        }
        catch(Exception ex)
    {
            ex.printStackTrace();
    }  
        factory = sslctx.getSocketFactory();
        
    }
    
    protected String host;
    protected int port;
    protected Socket socket = null;
    protected IOException socketError =null;
    protected boolean hasTimedOut = false;
    
    public static void setClassLoader(ClassLoader newLoader)
    {
        myClassLoader = newLoader;
    }
    
    private static ClassLoader getClassLoader()
    {
        if(myClassLoader == null)
            myClassLoader = ClassLoader.getSystemClassLoader();
        return myClassLoader;
    }
    
    public static void setDebugOn()
    {
        System.setProperty("javax.net.debug", "ssl handshake verbose");
    }
    
    public JndiSocketFactory()
    {
        
        
    }
    
    public static SocketFactory getDefault()
    {
        synchronized(JndiSocketFactory.class)
    {
            if(default_factory == null)
                default_factory = new JndiSocketFactory();
    }
        return default_factory;
    }
    
    
    
    public Socket createSocket(String hostName, int port2)
        throws IOException, UnknownHostException {
            //System.out.println("create s1 called");
            this.host = hostName;
            this.port = port2;
            this.socket = null;
            this.socketError = null;
            hasTimedOut = false;
            
            Thread r = new Thread(this);
            r.setDaemon(true);
            r.start();
            try
            {
                r.join(TIME);
                
            }
            catch(java.lang.InterruptedException ie)
            {
                r.interrupt();
            }
            if (socketError != null)
            {
                throw socketError;
            }
            if(socket == null)
            {
                hasTimedOut = true;
                throw new IOException("Socket connection timed out: "+ host + ":"+ port);
            }
            else
            {
                System.out.println("Connected");
            }
            
            return socket;
            
        }
    
    
    public Socket createSocket(InetAddress host, int port)
        throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port);
    }
    
    public Socket createSocket(InetAddress host, int port, InetAddress client_host, int client_port)
        throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port, client_host, client_port);
    }
    
    public Socket createSocket(String host, int port, InetAddress client_host, int client_port)
        throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port, client_host, client_port);
    }
    
    public Socket createSocket(Socket socket, String host, int port, boolean autoclose)
        throws IOException, UnknownHostException
    {
        return factory.createSocket(socket, host, port, autoclose);
    }
    
    public String[] getDefaultCipherSuites()
    {
        return factory.getDefaultCipherSuites();
    }
    
    public String[] getSupportedCipherSuites()
    {
        return factory.getSupportedCipherSuites();
    }
    
    public void run()
    {
        try
    {
        socket = factory.createSocket(host, port);
    }
        catch(IOException ioe)
    {
            socketError =ioe;
    }
        if(hasTimedOut)
        {
            try
        {
            this.socket.close();
        }
            catch(IOException ioe)
        {
                //eat it
        };
            socket = null;
            socketError = null;
        }
        return;
    }
    
    
}
