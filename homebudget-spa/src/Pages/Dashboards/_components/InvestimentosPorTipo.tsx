import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import topbar from "topbar";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import styles from "../Dashboards.module.scss";

type InvestimentosPorTipoData = {
  tipo: string;
  total: number;
};

export default function InvestimentosPorTipo() {
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const now = new Date();
  const currentYear = now.getFullYear();
  const currentMonth = now.getMonth();

  const [startDate, setStartDate] = useState(
    new Date(currentYear, currentMonth),
  );
  const [endDate, setEndDate] = useState(new Date(currentYear, currentMonth));

  const [chartData, setChartData] = useState({
    series: [] as number[],
    options: {
      chart: {
        type: "pie" as const,
        height: 350,
      },
      labels: [] as string[],
      dataLabels: {
        enabled: true,
        formatter: (val: number) => `${val.toFixed(1)}%`,
      },
      tooltip: {
        y: {
          formatter: (val: number) => `R$ ${val.toFixed(2)}`,
        },
      },
      colors: [
        "#FF5733",
        "#4CAF50",
        "#FFC107",
        "#2196F3",
        "#9C27B0",
        "#B11722",
        "#00BCD4",
        "#FFEB3B",
        "#E91E63",
        "#3F51B5",
        "#8BC34A",
        "#FF9800",
        "#795548",
        "#607D8B",
        "#F44336",
        "#673AB7",
        "#009688",
        "#CDDC39",
        "#9E9E9E",
        "#FF5722",
      ],
      legend: {
        position: "bottom" as const,
      },
    },
  });

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const anoInicio = startDate.getFullYear();
      const mesInicio = startDate.getMonth() + 1;
      const anoFim = endDate.getFullYear();
      const mesFim = endDate.getMonth() + 1;

      const url = `${apiUrl}/relatorios/investimentos-por-tipo?anoInicio=${anoInicio}&mesInicio=${mesInicio}&anoFim=${anoFim}&mesFim=${mesFim}`;

      const response = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const transactions: InvestimentosPorTipoData[] = response.data;

      const categories = transactions.map((t) => t.tipo);
      const investimentoData = transactions.map((t) => t.total);

      setChartData({
        series: investimentoData,
        options: {
          ...chartData.options,
          labels: categories,
        },
      });
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, [token, startDate, endDate, apiUrl]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleDateChange = (date: Date | null, type: "start" | "end") => {
    if (!date) return;

    if (type === "start") {
      setStartDate(date);
    } else {
      setEndDate(date);
    }
  };

  return (
    <div className={(styles.graficos, styles.card)}>
      <h4 className={styles.card_title}>Investimento por Tipo</h4>
      <div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
        <div>
          <label>Data Início:</label>
          <DatePicker
            selected={startDate}
            onChange={(date) => handleDateChange(date, "start")}
            dateFormat="MM/yyyy"
            showMonthYearPicker
            className={styles.datepicker}
          />
        </div>
        <div>
          <label>Data Fim:</label>
          <DatePicker
            selected={endDate}
            onChange={(date) => handleDateChange(date, "end")}
            dateFormat="MM/yyyy"
            showMonthYearPicker
            className={styles.datepicker}
          />
        </div>
      </div>
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="pie"
        height={400}
      />
    </div>
  );
}
