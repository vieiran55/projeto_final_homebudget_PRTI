import Avatar from "@/_components/Avatar/Avatar";
import styles from "./Header.module.scss";
import classnames from "classnames";
import { OffCanvas } from "@/_components/OffCanvas/OffCanvas";
import logo from "/imgs/HomeBudget.webp";

export default function Header() {
  return (
    <header className={classnames(styles.header)}>
      <div className="d-flex justify-content-between">
        <div className="d-flex flex-row-reverse align-items-center justify-content-center">
          {/* <h1 className={styles.titulo}>Nossas Finanças</h1> */}
          <img src={logo} className={classnames(styles.logo, "img-fluid")} />
          <div>
            <OffCanvas />
          </div>
        </div>
        <Avatar />
      </div>
    </header>
  );
}
