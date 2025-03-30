import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import topbar from "topbar";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import styles from "../Dashboards.module.scss";

type SaldoEvolucaoData = {
  anoMes: string;
  saldoMensal: number;
  saldoTotal: number;
  aporteMensal: number;
  totalInvestimentos: number;
};

export default function EvolucaoSaldo() {
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const now = new Date();
  const currentYear = now.getFullYear();
  const currentMonth = now.getMonth();

  const [startDate, setStartDate] = useState(
    new Date(currentYear - 1, currentMonth),
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
    series: [{ name: "Saldo Acumulado", data: [] as number[] }],
    options: {
      chart: {
        type: "line" as const,
        height: 350,
        background: "transparent",
      },
      xaxis: {
        categories: [] as string[],
      },
      yaxis: {
        title: {
          text: "Saldo (R$)",
        },
      },
      stroke: {
        curve: "smooth" as const,
        width: 3,
      },
      markers: {
        size: 5,
      },
      colors: ["#007bff", "#ff9900", "#28a745", "#903545"],
      tooltip: {
        y: {
          formatter: (val: number) => `R$ ${val.toFixed(2)}`,
        },
      },
      legend: {
        position: "bottom" as const,
      },
      annotations: {
        yaxis: [
          {
            y: 0,
            borderColor: "#ff0000",
            strokeDashArray: 5,
            label: {
              borderColor: "#ff0000",
              style: {
                color: "#fff",
                background: "#ff0000",
              },
              text: "R$ 0,00",
            },
          },
        ],
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

      const url = `${apiUrl}/relatorios/saldo-mensal-total?anoInicio=${anoInicio}&mesInicio=${mesInicio}&anoFim=${anoFim}&mesFim=${mesFim}`;

      const response = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data: SaldoEvolucaoData[] = response.data;

      setChartData({
        series: [
          { name: "Saldo do Mês", data: data.map((d) => d.saldoMensal) },
          { name: "Saldo Total", data: data.map((d) => d.saldoTotal) },
          { name: "Aporte Mensal", data: data.map((d) => d.aporteMensal) },
          {
            name: "Total Investimentos",
            data: data.map((d) => d.totalInvestimentos),
          },
        ],
        options: {
          ...chartData.options,
          xaxis: {
            categories: data.map((d) => d.anoMes),
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
      <h4 className={styles.card_tittle}>Evolução de Saldo Mensal</h4>
      {/* Filtros de Data */}
      <div className={styles.graficos_inputs_container}>
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

      {/* Gráfico */}
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="line"
      />
    </div>
  );
}
