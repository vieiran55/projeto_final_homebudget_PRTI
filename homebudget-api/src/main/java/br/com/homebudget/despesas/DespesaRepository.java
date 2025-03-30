package br.com.homebudget.despesas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<DespesaEntity, Long>, JpaSpecificationExecutor<DespesaEntity> {

    @Query("""
        SELECT d FROM DespesaEntity d 
        WHERE d.user.id = :userId
        AND (
            YEAR(d.data) < :anoAtual
            OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual)
        )
    """)
    Page<DespesaEntity> findPastUntilCurrentMonth(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(SUM(d.valor), 0) FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND YEAR(d.data) = :anoAtual
    AND MONTH(d.data) = :mesAtual
""")
    BigDecimal somarDespesasMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT COALESCE(SUM(d.valor), 0) FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND (YEAR(d.data) < :anoAtual OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual))
""")
    BigDecimal somarDespesasAteMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT 
        YEAR(d.data) as ano, 
        MONTH(d.data) as mes, 
        COALESCE(SUM(d.valor), 0) as total
    FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND d.data >= :dataInicio
    GROUP BY YEAR(d.data), MONTH(d.data)
    ORDER BY ano, mes
""")
    List<Object[]> getDespesasUltimos12Meses(
            @Param("userId") Long userId,
            @Param("dataInicio") LocalDate dataInicio);


    @Query("""
    SELECT 
        d.categoria AS categoria, 
        COALESCE(SUM(d.valor), 0) AS total
    FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND (
        (YEAR(d.data) > :anoInicio OR (YEAR(d.data) = :anoInicio AND MONTH(d.data) >= :mesInicio))
        AND (YEAR(d.data) < :anoFim OR (YEAR(d.data) = :anoFim AND MONTH(d.data) <= :mesFim))
    )
    GROUP BY d.categoria
    ORDER BY total DESC
""")
    List<Object[]> getDistribuicaoDespesasPorCategoria(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );

    @Query("""
    SELECT YEAR(d.data) AS ano, MONTH(d.data) AS mes, COALESCE(SUM(d.valor), 0) AS total
    FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND (
        (YEAR(d.data) > :anoInicio OR (YEAR(d.data) = :anoInicio AND MONTH(d.data) >= :mesInicio))
        AND (YEAR(d.data) < :anoFim OR (YEAR(d.data) = :anoFim AND MONTH(d.data) <= :mesFim))
    )
    GROUP BY YEAR(d.data), MONTH(d.data)
    ORDER BY ano, mes
""")
    List<Object[]> getDespesasMensaisGrafico(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );

    @Query("""
    SELECT 
        d.categoria AS categoria,
        SUM(CASE WHEN YEAR(d.data) = :ano1 AND MONTH(d.data) = :mes1 THEN d.valor ELSE 0 END) AS totalPeriodo1,
        SUM(CASE WHEN YEAR(d.data) = :ano2 AND MONTH(d.data) = :mes2 THEN d.valor ELSE 0 END) AS totalPeriodo2
    FROM DespesaEntity d
    WHERE d.user.id = :userId
    AND (
        (YEAR(d.data) = :ano1 AND MONTH(d.data) = :mes1) 
        OR 
        (YEAR(d.data) = :ano2 AND MONTH(d.data) = :mes2)
    )
    GROUP BY d.categoria
    ORDER BY d.categoria
""")
    List<Object[]> getComparacaoDespesasPorCategoria(
            @Param("userId") Long userId,
            @Param("ano1") int ano1,
            @Param("mes1") int mes1,
            @Param("ano2") int ano2,
            @Param("mes2") int mes2
    );

    @Query("""
    SELECT 
        YEAR(d.data) AS ano, MONTH(d.data) AS mes, 
        COALESCE(SUM(r.valor), 0) - COALESCE(SUM(d.valor), 0) AS saldo
    FROM ReceitaEntity r
    FULL JOIN DespesaEntity d ON YEAR(r.data) = YEAR(d.data) AND MONTH(r.data) = MONTH(d.data) AND r.user.id = d.user.id
    WHERE d.user.id = :userId OR r.user.id = :userId
    GROUP BY YEAR(d.data), MONTH(d.data)
    ORDER BY ano, mes
""")
    List<Object[]> getSaldoMensal(@Param("userId") Long userId);

    @Query("""
    SELECT YEAR(d.data) AS ano, MONTH(d.data) AS mes, SUM(d.valor) AS total
    FROM DespesaEntity d
    WHERE d.user.id = :userId
    GROUP BY YEAR(d.data), MONTH(d.data)
    ORDER BY ano DESC, mes DESC
""")
    List<Object[]> getHistoricoDespesas(@Param("userId") Long userId);
}

