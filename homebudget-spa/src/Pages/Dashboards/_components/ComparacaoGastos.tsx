import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import topbar from "topbar";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import styles from "../Dashboards.module.scss";

type ComparacaoGastosData = {
  categoria: string;
  totalAtual: number;
  totalAnterior: number;
};

export default function ComparacaoGastos() {
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const now = new Date();
  const currentYear = now.getFullYear();
  const currentMonth = now.getMonth();

  const [startDate, setStartDate] = useState(
    new Date(currentYear, currentMonth - 1),
  );
  const [endDate, setEndDate] = useState(new Date(currentYear, currentMonth));

  const handleDateChange = (date: Date | null, type: "start" | "end") => {
    if (!date) return;

    if (type === "start") {
      setStartDate(date);
    } else {
      setEndDate(date);
    }
  };

  const [chartData, setChartData] = useState({
    series: [
      { name: "Período Atual", data: [] as number[] },
      { name: "Período Anterior", data: [] as number[] },
    ],
    options: {
      chart: {
        type: "bar" as const,
        stacked: true,
        height: 350,
        background: "transparent",
      },
      xaxis: {
        categories: [] as string[],
      },
      yaxis: {
        title: { text: "Gastos (R$)" },
        labels: {
          formatter: (value: number) =>
            `R$ ${value.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        },
      },
      colors: ["#007bff", "#ff4560"],
      tooltip: {
        y: {
          formatter: (val: number) =>
            `R$ ${val.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (val: number) =>
          `R$ ${val.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
        style: {
          fontSize: "12px",
          colors: ["#111"],
        },
      },
      legend: {
        position: "bottom" as const,
      },
    },
  });

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const ano1 = startDate.getFullYear();
      const mes1 = startDate.getMonth() + 1;
      const ano2 = endDate.getFullYear();
      const mes2 = endDate.getMonth() + 1;

      const url = `${apiUrl}/relatorios/comparacao-gastos?ano1=${ano1}&mes1=${mes1}&ano2=${ano2}&mes2=${mes2}`;

      const response = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data: ComparacaoGastosData[] = response.data;

      setChartData({
        series: [
          { name: `Mês ${mes1}/${ano1}`, data: data.map((d) => d.totalAtual) },
          {
            name: `Mês ${mes2}/${ano2}`,
            data: data.map((d) => d.totalAnterior),
          },
        ],
        options: {
          ...chartData.options,
          xaxis: {
            categories: data.map((d) => d.categoria),
          },
        },
      });
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, [token, startDate, endDate]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div className={(styles.graficos, styles.card)}>
      <h4 className={styles.card_tittle}>Comparação de Gastos</h4>
      <div className={styles.graficos_inputs_container}>
        <div>
          <label>Período 1:</label>
          <DatePicker
            selected={startDate}
            onChange={(date) => handleDateChange(date, "start")}
            dateFormat="MM/yyyy"
            showMonthYearPicker
            className={styles.datepicker}
          />
        </div>
        <div>
          <label>Período 2:</label>
          <DatePicker
            selected={endDate}
            onChange={(date) => handleDateChange(date, "end")}
            dateFormat="MM/yyyy"
            showMonthYearPicker
            className={styles.datepicker}
          />
        </div>
      </div>
      <Chart options={chartData.options} series={chartData.series} type="bar" />
    </div>
  );
}
