package br.com.homebudget.shared.response;

import br.com.homebudget.shared.dto.MetaResponse;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        MetaResponse meta
) {}
