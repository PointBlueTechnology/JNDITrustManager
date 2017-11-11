# JNDI Dummy Trust Manager

A simple implementation of a JSSE trust manager that can be used to make LDAPS and LDAP TLS connections to secure LDAP servers
using the Native JNDI API without configuring trusted certificates.

This is useful for non-production enviornments and in situations where a full trust relationship is not needed. The code can easily be
extended to allow you to dynamically trust specific certificates instead of trusting all.
