INSERT INTO oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_secret_expires_at,
    client_name, client_authentication_methods, authorization_grant_types,
    redirect_uris, post_logout_redirect_uris, scopes,
    client_settings, token_settings
) VALUES (
    'client-001',
    'client',
    CURRENT_TIMESTAMP,
    '{noop}secret',
    NULL,
    'Demo Client',
    'client_secret_basic',
    'authorization_code,refresh_token,client_credentials',
    'http://127.0.0.1:8080/login/oauth2/code/my-client',
    'http://127.0.0.1:8080/logout-success',
    'openid,read',
    '{"requireAuthorizationConsent":true}',
    '{"accessTokenTimeToLive":"PT30M","refreshTokenTimeToLive":"PT8H","reuseRefreshTokens":false}'
);