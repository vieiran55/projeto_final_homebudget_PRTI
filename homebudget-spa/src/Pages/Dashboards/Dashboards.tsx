import ComparacaoGastos from "@/Pages/Dashboards/_components/ComparacaoGastos";
import DespesasPorCategoria from "@/Pages/Dashboards/_components/DespesasPorCategoria";
import EvolucaoSaldo from "@/Pages/Dashboards/_components/EvolucaoSaldo";
import ProjecaoFinanceira from "@/Pages/Dashboards/_components/ProjecaoFinanceira";
import ReceitasPorFonte from "@/Pages/Dashboards/_components/ReceitasPorFonte";
import Transacoes12Meses from "@/Pages/Dashboards/_components/Transacoes12Meses";
import styles from "./Dashboards.module.scss";
import InvestimentosPorTipo from "@/Pages/Dashboards/_components/InvestimentosPorTipo";
import classNames from "classnames";

export default function Dashboards() {
  return (
    <div className="container">
      <h1 className="text-center">Meus Dashboards</h1>
      <div className={styles.dashboards_container}>
        <div className={styles.graficos_col}>
          <Transacoes12Meses />
        </div>
        <div className={styles.graficos_row}>
          <DespesasPorCategoria />
          <ReceitasPorFonte />
          <InvestimentosPorTipo />
        </div>
        <div className={classNames(styles.graficos_row, styles.graficos_duplo)}>
          <EvolucaoSaldo />
          <ComparacaoGastos />
        </div>
        <div className={styles.graficos_col}>
          <ProjecaoFinanceira />
        </div>
      </div>
    </div>
  );
}
