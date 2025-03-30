import styles from "./Home.module.scss";
import vetor1 from "/imgs/img2.jpg";
import vetor2 from "/imgs/img3.jpg";

const Home = () => {
  return (
    <div className="container">
      <div className={styles.content}>
        <img src={vetor1} alt="Gestão Financeira" className={styles.image} />
        <div className={styles.text}>
          {" "}
          <h2>Bem-vindo ao HomeBudget</h2>
          <p>
            O HomeBudget é uma solução completa para gerenciamento de transações
            financeiras. Controle suas receitas e despesas de maneira simples e
            eficiente.
          </p>
        </div>
      </div>

      <div className={styles.content}>
        <div className={styles.text}>
          <h2>Organize sua vida financeira</h2>
          <p>
            Com gráficos intuitivos e ferramentas de categorização, você pode
            visualizar seus gastos, identificar padrões e tomar decisões mais
            inteligentes.
          </p>
        </div>
        <img
          src={vetor2}
          alt="Organização Financeira"
          className={styles.image}
        />
      </div>
    </div>
  );
};

export default Home;
