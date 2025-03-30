import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import topbar from "topbar";
import styles from "../Dashboards.module.scss";

type ProjecaoFinanceiraData = {
  ano: number;
  mes: number;
  saldoProjetado: number;
};

export default function ProjecaoFinanceira() {
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const [mesesPrevisao, setMesesPrevisao] = useState(6);
  const [chartData, setChartData] = useState({
    series: [{ name: "Saldo Projetado", data: [] as number[] }],
    options: {
      chart: {
        type: "line" as const,
        height: 400,
        background: "transparent",
      },
      xaxis: {
        categories: [] as string[],
        labels: {
          style: { fontSize: "14px", fontWeight: 600, colors: "#555" },
        },
      },
      yaxis: {
        title: {
          text: "Saldo (R$)",
          style: { fontSize: "16px", fontWeight: 700, color: "#333" },
        },
        labels: {
          style: { fontSize: "14px", fontWeight: 600, color: "#666" },
          formatter: (value: number) =>
            `R$ ${value.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        },
      },
      tooltip: {
        theme: "dark",
        style: { fontSize: "14px" },
        y: {
          formatter: (val: number) =>
            `R$ ${val.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        },
      },
      dataLabels: {
        enabled: true,
        background: { enabled: true, foreColor: "#fff" },
        formatter: (val: number) =>
          `R$ ${val.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        style: { fontSize: "14px", fontWeight: 700 },
      },
      stroke: {
        curve: "smooth" as const,
        width: 3,
        colors: ["#007bff"], // Azul vibrante
      },
      markers: {
        size: 6,
        colors: ["#007bff"],
        strokeWidth: 2,
        strokeColors: "#fff",
      },
      grid: {
        borderColor: "#ddd",
        strokeDashArray: 5,
      },
      legend: {
        position: "top" as const,
        fontSize: "14px",
        fontWeight: 600,
      },
    },
  });

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const url = `${apiUrl}/relatorios/projecao-financeira?mesesProjecao=${mesesPrevisao}
      `;
      const response = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data: ProjecaoFinanceiraData[] = response.data;

      console.log("🔹 Dados recebidos:", data);

      setChartData((prevChartData) => ({
        series: [
          { name: "Saldo Projetado", data: data.map((d) => d.saldoProjetado) },
        ],
        options: {
          ...prevChartData.options, // Garante que usa o estado atualizado
          xaxis: {
            ...prevChartData.options.xaxis,
            categories: data.map((d) => `${d.mes}/${d.ano}`),
          },
        },
      }));
    } catch (error) {
      console.error("Erro ao buscar projeção financeira:", error);
    } finally {
      topbar.hide();
    }
  }, [token, mesesPrevisao]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div className={(styles.graficos, styles.card)}>
      <h4 className={styles.card_tittle}>Projeção Financeira de Saldo</h4>

      <div
        style={{
          display: "flex",
          justifyContent: "center",
          gap: "10px",
          marginBottom: "20px",
        }}
      >
        <label style={{ fontWeight: "bold" }}>Previsão para:</label>
        <select
          value={mesesPrevisao}
          onChange={(e) => setMesesPrevisao(Number(e.target.value))}
          style={{
            padding: "8px",
            borderRadius: "5px",
            border: "1px solid #ccc",
          }}
        >
          <option value={3}>3 meses</option>
          <option value={6}>6 meses</option>
          <option value={9}>9 meses</option>
          <option value={12}>12 meses</option>
        </select>
      </div>

      {/* Gráfico */}
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="line"
        height={400}
      />
    </div>
  );
}
