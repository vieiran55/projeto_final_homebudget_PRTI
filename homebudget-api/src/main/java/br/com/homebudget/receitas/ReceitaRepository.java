package br.com.homebudget.receitas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReceitaRepository extends JpaRepository<ReceitaEntity, Long>, JpaSpecificationExecutor<ReceitaEntity> {

    @Query("""
        SELECT d FROM ReceitaEntity d 
        WHERE d.user.id = :userId
        AND (
            YEAR(d.data) < :anoAtual
            OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual)
        )
    """)
    Page<ReceitaEntity> findPastUntilCurrentMonth(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(SUM(d.valor), 0) FROM ReceitaEntity d
    WHERE d.user.id = :userId
    AND YEAR(d.data) = :anoAtual
    AND MONTH(d.data) = :mesAtual
""")
    BigDecimal somarReceitasMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT COALESCE(SUM(d.valor), 0) FROM ReceitaEntity d
    WHERE d.user.id = :userId
    AND (YEAR(d.data) < :anoAtual OR (YEAR(d.data) = :anoAtual AND MONTH(d.data) <= :mesAtual))
""")
    BigDecimal somarReceitasAteMesAtualByUser(
            @Param("userId") Long userId,
            @Param("anoAtual") int anoAtual,
            @Param("mesAtual") int mesAtual
    );

    @Query("""
    SELECT 
        YEAR(r.data) as ano, 
        MONTH(r.data) as mes, 
        COALESCE(SUM(r.valor), 0) as total
    FROM ReceitaEntity r
    WHERE r.user.id = :userId
    AND r.data >= :dataInicio
    GROUP BY YEAR(r.data), MONTH(r.data)
    ORDER BY ano, mes
""")
    List<Object[]> getReceitasUltimos12Meses(@Param("userId") Long userId, @Param("dataInicio") LocalDate dataInicio);


    @Query("""
    SELECT 
        r.fonte AS fonte, 
        COALESCE(SUM(r.valor), 0) AS total
    FROM ReceitaEntity r
    WHERE r.user.id = :userId
    AND (
        (YEAR(r.data) > :anoInicio OR (YEAR(r.data) = :anoInicio AND MONTH(r.data) >= :mesInicio))
        AND (YEAR(r.data) < :anoFim OR (YEAR(r.data) = :anoFim AND MONTH(r.data) <= :mesFim))
    )
    GROUP BY r.fonte
    ORDER BY total DESC
""")
    List<Object[]> getDistribuicaoReceitasPorFonte(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );


    @Query("""
    SELECT YEAR(r.data) AS ano, MONTH(r.data) AS mes, COALESCE(SUM(r.valor), 0) AS total
    FROM ReceitaEntity r
    WHERE r.user.id = :userId
    AND (
        (YEAR(r.data) > :anoInicio OR (YEAR(r.data) = :anoInicio AND MONTH(r.data) >= :mesInicio))
        AND (YEAR(r.data) < :anoFim OR (YEAR(r.data) = :anoFim AND MONTH(r.data) <= :mesFim))
    )
    GROUP BY YEAR(r.data), MONTH(r.data)
    ORDER BY ano, mes
""")
    List<Object[]> getReceitasMensaisGrafico(
            @Param("userId") Long userId,
            @Param("anoInicio") int anoInicio,
            @Param("mesInicio") int mesInicio,
            @Param("anoFim") int anoFim,
            @Param("mesFim") int mesFim
    );

    @Query("""
    SELECT YEAR(r.data) AS ano, MONTH(r.data) AS mes, SUM(r.valor) AS total
    FROM ReceitaEntity r
    WHERE r.user.id = :userId
    GROUP BY YEAR(r.data), MONTH(r.data)
    ORDER BY ano DESC, mes DESC
""")
    List<Object[]> getHistoricoReceitas(@Param("userId") Long userId);
}
