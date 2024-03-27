
use `spring_cloud_xuxiaowei`;

SET NAMES utf8mb4;

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------
INSERT INTO `oauth2_registered_client` VALUES ('8f91c2d6-8a44-4c05-a098-8d271112c2bc', 'messaging-client', '2024-03-08 10:30:12', '{noop}secret', '2034-03-06 10:30:12', '8f91c2d6-8a44-4c05-a098-8d271112c2bc', 'client_secret_basic', 'refresh_token,client_credentials,authorization_code', 'http://127.0.0.1:8080/authorized,https://home.baidu.com/home/index/contact_us,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc', NULL, 'openid,profile,message.read,message.write', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
