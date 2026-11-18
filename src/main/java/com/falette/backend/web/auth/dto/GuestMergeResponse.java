package com.falette.backend.web.auth.dto;

/** POST /api/v1/auth/merge-guest 응답: 구글 계정으로 옮긴 비회원 리딩 개수 */
public record GuestMergeResponse(int mergedCount) {
}
