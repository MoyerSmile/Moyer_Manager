package sl.util;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
    private static final String profiles = "slmoyer";
    private static final String JWT_TYPE = "JWT";
    private static final String JWT_ALG  = "HS256";

    /**
     * 由字符串生成加密key
     * 
     * @return
     */
    private static SecretKey generalKey() {
        String stringKey = profiles;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "AES");
        return key;
    }

    /**
     * 创建jwt
     * 
     * @param id
     * @param account
     *            面向的用户
     * @param claims
     *            自定义内容
     * @param ttlMillis
     *            过期时长毫秒
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String account,
            Map<String, Object> claims, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();

        JwtBuilder builder = Jwts.builder()
            .setHeaderParam("type", JWT_TYPE)
            .setHeaderParam("alg", JWT_ALG)
            .setClaims(claims)
            .setId(id)
            .setIssuedAt(now)
            .setSubject(account)
            .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解析jwt
     * 
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(jwt)
            .getBody();
        return claims;
    }

    // public static void main(String[] args) {
    // try {
    // Map<String, Object> claims = new HashMap<>();
    // claims.put("iss", "777");// 发行人
    // String token = TokenUtil.createJWT("233", "payload", claims,
    // 1000 * 60);
    // System.out.println(token);
    //
    // Claims c = TokenUtil.parseJWT(token);
    // System.out.println(c.getId());
    // System.out.println(c.getSubject());//面向的用户
    // System.out.println(c.getIssuer());
    // System.out.println(c.getIssuedAt());// 发行时间
    // System.out.println(c.getExpiration());// 过期时间
    //
    // long nowMillis = System.currentTimeMillis();
    // long expMillis = nowMillis + 100000 * 60;
    // Date exp = new Date(expMillis);
    // c.setExpiration(exp);
    // System.err.println(c.getExpiration());
    // Claims cc = TokenUtil.parseJWT(token);
    // System.err.println(cc.getExpiration());
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

}
