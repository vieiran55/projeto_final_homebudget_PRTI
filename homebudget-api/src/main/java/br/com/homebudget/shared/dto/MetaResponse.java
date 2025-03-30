package br.com.homebudget.shared.dto;

public record MetaResponse(Number totalItems, Number totalPages, Number currentPage, Number perPage) {
}
