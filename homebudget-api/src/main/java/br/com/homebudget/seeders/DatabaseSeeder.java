package br.com.homebudget.seeders;

import br.com.homebudget.despesas.DespesaEntity;
import br.com.homebudget.despesas.DespesaRepository;
import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;
import br.com.homebudget.receitas.ReceitaEntity;
import br.com.homebudget.receitas.ReceitaRepository;
import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import br.com.homebudget.investimentos.InvestimentoEntity;
import br.com.homebudget.investimentos.InvestimentoRepository;
import br.com.homebudget.investimentos.enums.TipoInvestimentoEnum;
import br.com.homebudget.users.UserEntity;
import br.com.homebudget.users.UserRepository;
import br.com.homebudget.users.UserService;
import br.com.homebudget.users.dto.UserInputDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final DespesaRepository despesaRepository;
    private final ReceitaRepository receitaRepository;
    private final InvestimentoRepository investimentoRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public DatabaseSeeder(DespesaRepository despesaRepository,
                          ReceitaRepository receitaRepository,
                          InvestimentoRepository investimentoRepository,
                          UserRepository userRepository,
                          UserService userService) {
        this.despesaRepository = despesaRepository;
        this.receitaRepository = receitaRepository;
        this.investimentoRepository = investimentoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedDespesas();
        seedReceitas();
        seedInvestimentos();
    }

    private void seedUsers() {
        if (userRepository.findByEmail("usuario@example.com").isEmpty()) {
            UserInputDTO userInputDTO = new UserInputDTO(
                    null,
                    "Usuário Exemplo",
                    "usuario@example.com",
                    "123456"
            );

            userService.create(userInputDTO);
        }
    }

    private void seedDespesas() {
        userRepository.findByEmail("usuario@example.com").ifPresent(user -> {

            LocalDate hoje = LocalDate.now();
            LocalDate ultimoDiaMesAnterior = hoje.withDayOfMonth(1).minusDays(1);
            int diasDisponiveis = (int) ChronoUnit.DAYS.between(ultimoDiaMesAnterior.minusDays(365), ultimoDiaMesAnterior);



            if (despesaRepository.count() < 100) {
                List<CategoriaDespesaEnum> categorias = List.of(CategoriaDespesaEnum.values());

                for (int i = 0; i < 100; i++) {
                    DespesaEntity despesa = new DespesaEntity();
                    despesa.setUser(user);
                    despesa.setCategoria(categorias.get(random.nextInt(categorias.size())));
                    despesa.setValor(BigDecimal.valueOf(random.nextDouble() * 500 + 10)); // Valores entre 10 e 510
                    despesa.setDescricao("Despesa aleatória #" + (i + 1));
                    despesa.setData(ultimoDiaMesAnterior.minusDays(random.nextInt(diasDisponiveis)));

                    despesaRepository.save(despesa);
                }
            }
        });
    }

    private void seedReceitas() {
        userRepository.findByEmail("usuario@example.com").ifPresent(user -> {
            LocalDate hoje = LocalDate.now();
            LocalDate ultimoDiaMesAnterior = hoje.withDayOfMonth(1).minusDays(1);
            int diasDisponiveis = (int) ChronoUnit.DAYS.between(ultimoDiaMesAnterior.minusDays(365), ultimoDiaMesAnterior);

            if (receitaRepository.count() < 30) {
                List<FonteReceitaEnum> fontes = List.of(FonteReceitaEnum.values());

                for (int i = 0; i < 30; i++) {
                    ReceitaEntity receita = new ReceitaEntity();
                    receita.setUser(user);
                    receita.setFonte(fontes.get(random.nextInt(fontes.size())));
                    receita.setValor(BigDecimal.valueOf(random.nextDouble() * 2500 + 1000)); // Valores entre 100 e 1100
                    receita.setDescricao("Receita aleatória #" + (i + 1));
                    receita.setData(ultimoDiaMesAnterior.minusDays(random.nextInt(diasDisponiveis)));

                    receitaRepository.save(receita);
                }
            }
        });
    }

    private void seedInvestimentos() {
        userRepository.findByEmail("usuario@example.com").ifPresent(user -> {
            LocalDate hoje = LocalDate.now();
            LocalDate ultimoDiaMesAnterior = hoje.withDayOfMonth(1).minusDays(1);
            int diasDisponiveis = (int) ChronoUnit.DAYS.between(ultimoDiaMesAnterior.minusDays(365), ultimoDiaMesAnterior);

            if (investimentoRepository.count() < 5) {
                List<TipoInvestimentoEnum> tipos = List.of(TipoInvestimentoEnum.values());

                for (int i = 0; i < 30; i++) {
                    InvestimentoEntity investimento = new InvestimentoEntity();
                    investimento.setUser(user);
                    investimento.setTipo(tipos.get(random.nextInt(tipos.size())));
                    investimento.setValorInicial(BigDecimal.valueOf(random.nextDouble() * 500 + 500)); // Valores entre 500 e 5500
                    investimento.setValorAtual(investimento.getValorInicial().multiply(BigDecimal.valueOf(1 + random.nextDouble() * 0.1))); // Valor atual entre 100% e 110% do valor inicial
                    investimento.setDescricao("Investimento aleatório #" + (i + 1));
                    investimento.setData(ultimoDiaMesAnterior.minusDays(random.nextInt(diasDisponiveis)));

                    investimentoRepository.save(investimento);
                }
            }
        });
    }
}