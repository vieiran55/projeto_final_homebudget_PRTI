import styles from "./OffCanvas.module.scss";
import logo from "/imgs/HomeBudget.webp";
import classNames from "classnames";

import { useState } from "react";
import OffCanvasDefault from "@/_components/OffCanvas/_components/OffCanvasPerfilDefault";

export const OffCanvas = () => {
  const [show, setShow] = useState(false);

  return (
    <>
      <div className="header-menu-trigger">
        <button
          className={classNames(styles.color_button, "br-button small circle")}
          type="button"
          aria-label="Menu"
          onClick={() => setShow(true)}
        >
          <i className="fas fa-bars" aria-hidden="true"></i>
        </button>
      </div>

      <div
        className={classNames(styles.overlay, { [styles.show]: show })}
        onClick={() => setShow(false)}
      ></div>

      <aside
        className={classNames("offcanvas offcanvas-start", styles.offcanvas, {
          show,
        })}
        tabIndex={-1}
        style={{ visibility: show ? "visible" : "hidden" }}
      >
        <div className="offcanvas-header">
          <div className="my-4 d-flex justify-content-center">
            <img src={logo} className="img-fluid" alt="Home Budget" />
          </div>
          <button
            type="button"
            className={classNames("btn", styles.botao_close)}
            aria-label="Close"
            onClick={() => setShow(false)}
          >
            X
          </button>
        </div>

        <OffCanvasDefault />
      </aside>
    </>
  );
};
