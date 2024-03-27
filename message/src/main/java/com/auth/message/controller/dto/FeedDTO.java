package com.auth.message.controller.dto;

import java.util.List;

public record FeedDTO(List<FeedItemDTO> feedItem, int page, int pageSoze, int totalPages,
                      long totalElements) {
}
