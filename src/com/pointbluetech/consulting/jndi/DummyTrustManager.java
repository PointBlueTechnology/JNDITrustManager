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
//
//  DummyTrustManager.java
//  This class is used so that LDAPS connections can be made without verifying the cert
//
//  Created by Jerry Combs on 7/11/05.

package com.pointbluetech.consulting.jndi;

import javax.net.ssl.*;
import java.security.cert.*;
import java.security.*;


public class DummyTrustManager implements X509TrustManager
{
    
    // SSLContext ctx = SSLContext.getInstance("TLS");
    // ctx.init(null, myTM, null);
    
    //ssl.TrustManagerFactory.algorithm=SunX509
    
    //Security.setProperty("ssl.TrustManagerFactory.algorithm",
    //                     "dummyTrust");
    
    public void checkClientTrusted(X509Certificate[] chain, String authType)
    throws CertificateException
{
    return;
}

public void checkServerTrusted(X509Certificate[] chain, String authType)
throws CertificateException
{
    return;
}

public X509Certificate[] getAcceptedIssuers()
{
    //throw new RuntimeException("NOT IMPLEMENTED");
    return new X509Certificate[0];
}
}
