import styles from "../../GerenciamentoTransacoes.module.scss";
import { Link } from "react-router-dom";

interface Props {
  valor: number;
  titulo: string;
  classe: string;
  icon?: string;
}
export default function DadosCarteira({ valor, titulo, classe, icon }: Props) {
  const formattedSaldo = valor.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });

  console.log(valor);

  return (
    <div className={styles[classe]}>
      <div className={valor <= 0 ? styles.negativo : ""}>
        {titulo} {formattedSaldo}
      </div>
      <Link to={"/investimentos"} className={styles.wiggle_icon}>
        <i className={icon}></i>
      </Link>
    </div>
  );
}
