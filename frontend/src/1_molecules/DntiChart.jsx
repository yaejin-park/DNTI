import { useState, useEffect, useRef  } from "react";
import styles from "./DntiChart.module.css";
import {Bar} from "react-chartjs-2"
import {Chart as ChartJS} from "chart.js/auto"

function Chart({rank,num}) {
  let bgColor = []
  const chartRef = useRef();

  for (let i = 0; i < rank.length; i ++){
    if (i === num) {
      bgColor[i]="rgba(122, 8, 255, 0.8)"
    } else {
      bgColor[i]="rgba(200, 200, 200, 0.8)"
    }
  }


  const [userData, setUserData] = useState({
    labels: rank.map((data) => data.dongName),
    datasets: [{
      // label: null,
      data: rank.map((data) => data.totalScore),
      backgroundColor: bgColor,
      barPercentage: 0.5,
      // borderRadius: 20,
      // borderSkipped: false,
    }],
  })
  const options = {
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      y: {
        beginAtZero: false,
        grid: {
          borderColor: "#e0e0e0",
          color: "#f0f0f0"
        }
      },
      x: {
        grid: {
          borderColor: "#d0d0d0",
          drawOnChartArea: false, 
        }
      }
    },
  }
  useEffect(() => {
    setUserData({
      labels: rank.map((data) => data.dongName),
      datasets: [{
        // label: null,
        data: rank.map((data) => data.totalScore),
        backgroundColor: bgColor,
        duration: 0.1
      }]
    });
  }, [num]);

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <Bar data={userData} options={options} className={styles.chart}></Bar>
      </div>
    </div>
  );
}
export default Chart
