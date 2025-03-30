import styles from "./BreadCrumps.module.scss";

export default function BreadCrumps() {
  return (
    <nav aria-label="breadcrumb" className={styles.breadCrumps}>
      <ol className="breadcrumb">
        <li className="breadcrumb-item">
          <i className="fas fa-home"></i>
        </li>
        <li className="breadcrumb-item" aria-current="page">
          Controle Financeiro
        </li>
        <li className="breadcrumb-item" aria-current="page">
          Janeiro de 2025
        </li>
      </ol>
    </nav>
  );
}
