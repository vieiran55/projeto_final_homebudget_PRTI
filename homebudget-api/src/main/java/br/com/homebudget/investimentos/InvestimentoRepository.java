package br.com.homebudget.investimentos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvestimentoRepository extends JpaRepository<InvestimentoEntity, Long>, JpaSpecificationExecutor<InvestimentoEntity> {

    @Query("""
        SELECT d FROM InvestimentoEntity d 
        WHERE d.user.id = :userId
        AND (
            YEAR(d.data) < :anoAtual
            OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual)
        )
    """)
    Page<InvestimentoEntity> findPastUntilCurrentMonth(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(SUM(d.valorAtual), 0) FROM InvestimentoEntity d
    WHERE d.user.id = :userId
    AND YEAR(d.data) = :anoAtual
    AND MONTH(d.data) = :mesAtual
""")
    BigDecimal somarInvestimentosMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT COALESCE(SUM(d.valorAtual), 0) FROM InvestimentoEntity d
    WHERE d.user.id = :userId
    AND (YEAR(d.data) < :anoAtual OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual))
""")
    BigDecimal somarInvestimentosAteMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT 
        YEAR(d.data) as ano, 
        MONTH(d.data) as mes, 
        COALESCE(SUM(d.valorAtual), 0) as total
    FROM InvestimentoEntity d
    WHERE d.user.id = :userId
    AND d.data >= :dataInicio
    GROUP BY YEAR(d.data), MONTH(d.data)
    ORDER BY ano, mes
""")
    List<Object[]> getInvestimentosUltimos12Meses(
            @Param("userId") Long userId,
            @Param("dataInicio") LocalDate dataInicio);

    @Query("""
    SELECT 
        d.tipo AS tipo, 
        COALESCE(SUM(d.valorAtual), 0) AS total
    FROM InvestimentoEntity d
    WHERE d.user.id = :userId
    AND (
        (YEAR(d.data) > :anoInicio OR (YEAR(d.data) = :anoInicio AND MONTH(d.data) >= :mesInicio))
        AND (YEAR(d.data) < :anoFim OR (YEAR(d.data) = :anoFim AND MONTH(d.data) <= :mesFim))
    )
    GROUP BY d.tipo
    ORDER BY total DESC
""")
    List<Object[]> getDistribuicaoInvestimentoPorTipo(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );

    @Query("""
    SELECT YEAR(i.data) AS ano, MONTH(i.data) AS mes, COALESCE(SUM(i.valorAtual), 0) AS total
    FROM InvestimentoEntity i
    WHERE i.user.id = :userId
    AND (
        (YEAR(i.data) > :anoInicio OR (YEAR(i.data) = :anoInicio AND MONTH(i.data) >= :mesInicio))
        AND (YEAR(i.data) < :anoFim OR (YEAR(i.data) = :anoFim AND MONTH(i.data) <= :mesFim))
    )
    GROUP BY YEAR(i.data), MONTH(i.data)
    ORDER BY ano, mes
""")
    List<Object[]> getInvestimentosMensaisGrafico(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );
}
