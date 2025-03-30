import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import topbar from "topbar";
import styles from "../Dashboards.module.scss";

type TransactionData = {
  ano: number;
  mes: number;
  totalReceitas: number;
  totalDespesas: number;
  totalInvestimentos: number;
};

export default function Transacoes12Meses() {
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const response = await axios.get(
        `${apiUrl}/relatorios/ultimos-12-meses/`,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );

      const transactions: TransactionData[] = response.data;

      const categories = transactions.map((t) =>
        new Date(t.ano, t.mes - 1).toLocaleString("pt-BR", {
          month: "short",
          year: "numeric",
        }),
      );
      const receitasData = transactions.map((t) => t.totalReceitas);
      const despesasData = transactions.map((t) => t.totalDespesas);
      const InvestimentosData = transactions.map((t) => t.totalInvestimentos);

      setChartData((prev) => ({
        ...prev,
        series: [
          { name: "Receitas", data: receitasData },
          { name: "Despesas", data: despesasData },
          { name: "Investimentos", data: InvestimentosData },
        ],
        options: {
          ...prev.options,
          xaxis: { categories },
        },
      }));
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, [token]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const [chartData, setChartData] = useState({
    series: [
      { name: "Receitas", data: [] as number[] },
      { name: "Despesas", data: [] as number[] },
      { name: "Investimentos", data: [] as number[] },
    ],
    options: {
      chart: {
        type: "bar" as const,
        height: 350,
        background: "transparent",
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: "55%",
          borderRadius: 5,
          borderRadiusApplication: "end" as const,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        show: true,
        width: 2,
        colors: ["transparent"],
      },
      xaxis: {
        categories: [] as string[],
      },
      yaxis: {
        title: {
          text: "R$ (milhares)",
        },
      },
      fill: {
        opacity: 1,
      },
      tooltip: {
        y: {
          formatter: (val: number) => `R$ ${val.toFixed(2)}`,
        },
      },
      colors: ["#4CAF50", "#FF5733", "#FFC300"],
      legend: {
        position: "top" as const,
      },
    },
  });

  return (
    <div className={(styles.graficos, styles.card)}>
      <h4 className={styles.card_tittle}>
        Receitas x Despesas x Investimentos (Últimos 12 Meses)
      </h4>
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="bar"
        height={350}
      />
    </div>
  );
}
