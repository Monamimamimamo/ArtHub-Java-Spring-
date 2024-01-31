package authorization;


public class Auth {

    private String access_token;
    private String refresh_token;

    private String email;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getEmail() {
        return email;
    }

    public Auth(String access_token, String refresh_token, String email) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.email = email;
    }

    public Auth() {
    }
}
