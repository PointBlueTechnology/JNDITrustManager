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

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.NamingEnumeration;
import javax.naming.NameNotFoundException;

/**
 *
 * @author jcombs
 */
public class jndiExample
{


  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        jndiExample theOne = new jndiExample();
        theOne.ldapExample("192.168.101.86", "cn=admin,o=commoveo", "dittibop",
                "cn=aGroup,ou=groups,dc=pbad,dc=pointbluetech,dc=com");

    }

    public void ldapExample(String host, String userDN, String password, String objectDN)
    {
        try
        {

            LdapContext dirContext = getLdapCtx(host, userDN, password, true, "636", true);
            boolean foo = dnExists( objectDN, dirContext);

        }catch(NamingException ex)
        {
          ex.printStackTrace();
        }
    }

    private boolean dnExists(String dn, LdapContext dirContext) throws NamingException
    {
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.OBJECT_SCOPE);
        try
        {
            NamingEnumeration<SearchResult> resEnum = dirContext.search(dn, "objectClass=*", cons);

        }
        catch (NameNotFoundException nnf)
        {
            return false;
        }

        System.out.println("Container exists: " + dn);
        return true;

    }

   

    public LdapContext getLdapCtx(String ldapHost, String loginDN, String pwd,
                                  boolean ssl, String ldapPort, boolean trustAllCerts)
    {
        LdapContext ldapCtx = null;

        try
        {
            // Create a Hashtable object.
            Hashtable env = new Hashtable(5, 0.75f);

            if (ssl)
            {
                // ldapPort     = LdapCtx.DEFAULT_SSL_PORT;
                env.put(Context.SECURITY_PROTOCOL, "ssl");

                if (trustAllCerts)
                {
                    env.put("java.naming.ldap.factory.socket",
                            "com.novell.consulting.jndi.JndiSocketFactory");
                }
            }
            else
            {
                //  ldapPort     = LdapCtx.DEFAULT_PORT;
            }

            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + ldapHost + ":"
                    + ldapPort);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, loginDN);
            env.put(Context.SECURITY_CREDENTIALS, pwd);

            // Construct an LdapContext object.       
            ldapCtx = new InitialLdapContext(env, null);
        }
        catch (NamingException e)
        {
            System.out.println("Error getting LdapCtx:  ");
            e.printStackTrace();
            //TODO: should let this flow through
        }

        return ldapCtx;
    }

}
