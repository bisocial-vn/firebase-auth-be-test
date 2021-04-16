package com.firebase.auth.example.core.jwt;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class TokenVerify {

	private static volatile TokenVerify INSTANCE = null;
	private static String RSA_ALGORITHM = "RSA";

	private TokenVerify() {
		if (INSTANCE != null) {
			throw new RuntimeException("Already initial.");
		}
	}

	public static TokenVerify getInstance() {
		if (INSTANCE == null) {
			synchronized (TokenVerify.class) {
				if (INSTANCE == null) {
					INSTANCE = new TokenVerify();
				}
			}
		}
		return INSTANCE;
	}

	public Map<String, Object> verifyToken(PublicKey publicKey, String verifyingToken) throws RuntimeException {
		if (publicKey == null || !RSA_ALGORITHM.equals(publicKey.getAlgorithm())) {
			throw new RuntimeException("Invalid public key.");
		}
		JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();
		jwtParserBuilder.setSigningKey(publicKey);
		try {
			Jws<Claims> jwtClaims = jwtParserBuilder.build().parseClaimsJws(verifyingToken);
			Claims bodyPayload = jwtClaims.getBody();
			Set<Entry<String, Object>> allClaims = bodyPayload.entrySet();
			HashMap<String, Object> claimsMap = new HashMap<String, Object>();
			if (!allClaims.isEmpty()) {
				for (Entry<String, Object> entry : allClaims) {
					claimsMap.put(entry.getKey(), entry.getValue());
				}
			}
			return claimsMap;
		} catch (SignatureException ex) {
			throw new RuntimeException("Invalid signature.");
		} catch (ExpiredJwtException ex) {
			throw new RuntimeException("Token expired.");
		} catch (MalformedJwtException ex) {
			throw new RuntimeException("Invalid token.");
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Invalid token.");
		}
	}

}
