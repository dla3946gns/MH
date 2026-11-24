# Runtime configuration and security

The application now targets Java 17, Jakarta Servlet 6.1 and Tomcat 11.
Build with `mvn clean verify` from this directory.

Set the following environment variables in the application server process:

| Variable | Purpose |
|---|---|
| MH_DB_URL | MySQL JDBC URL (use TLS for remote production connections) |
| MH_DB_USER | Restricted database account |
| MH_DB_PASSWORD | Database password |
| MH_UPLOAD_DIR | Writable upload directory |
| MH_SMS_API_KEY | SMS provider API key |
| MH_SMS_API_SECRET | SMS provider API secret |
| MH_SMTP_HOST | SMTP server supporting STARTTLS on port 587 |
| MH_SMTP_USERNAME | SMTP account |
| MH_SMTP_PASSWORD | SMTP credential |
| MH_SMTP_FROM | Authorized sender email |

Do not commit real credentials or include them in logs. Rotate/revoke all
previously committed credentials with the providers; removing literals from
current source does not remove them from Git history. Existing production
deployment settings must be migrated before deploying the updated WAR.

CI uses an isolated MySQL 8.4 container, verifies JDBC and logging, then
deploys the WAR to Tomcat 11 and requests /goIndex to check JSP rendering.
This is not a comprehensive functional or security test. In particular,
validate existing database compatibility, uploads, login/password storage,
authorization, SMS and mail flows, and audit vendored JavaScript/JARs
before deploying publicly. Do not dismiss Dependabot alerts without
verifying the applicable fix.
