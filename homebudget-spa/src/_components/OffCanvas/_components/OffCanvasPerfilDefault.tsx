import styles from "../OffCanvas.module.scss";

import OffCanvasMenuItem from "./OffCanvasMenuItem";
import OffCanvasSocialMediaIcon from "./OffCanvasSocialMediaIcon";

const menuItems = [
  { icon: "fas fa-home fa-2x", label: "Página Inicial", link: "/" },
  {
    icon: "fas fa-cash-register fa-2x",
    label: "Gerenciamento de Transações",
    link: "/gerenciamento-transacoes",
  },
  // {
  //   icon: "fas fa-chart-line fa-2x",
  //   label: "Investimentos",
  //   link: "/investimentos",
  // },
  {
    icon: "fas fa-chart-pie fa-2x",
    label: "Dashboards",
    link: "/dashboards",
  },
];

const socialMedia = [
  {
    nome: "Facebook",
    icon: "fab fa-facebook-f fa-2x",
    link: "https://www.facebook.com/Secretariageraldapresidencia",
  },
  {
    nome: "Instagram",
    icon: "fab fa-instagram fa-2x",
    link: "https://www.instagram.com/sgpresidencia",
  },
  {
    nome: "YouTube",
    icon: "fab fa-youtube fa-2x",
    link: "https://www.youtube.com/@SGPR",
  },
];

export default function OffCanvasPerfilDefault() {
  return (
    <>
      <nav className="offcanvas-body p-0">
        <div className="br-list" role="list">
          <span className="br-divider"></span>
          {menuItems.map((item, index) => (
            <OffCanvasMenuItem key={index} {...item} />
          ))}
        </div>
      </nav>

      <section>
        <span className="br-divider"></span>
        <div className={styles.offcanvas_menu_itens_links_footer}>
          <div className="container">
            <p className="p-2 pb-2 mb-3 text-center">Redes Sociais</p>
            <div className="d-flex align-items-center justify-content-around">
              {socialMedia.map((item, index) => (
                <OffCanvasSocialMediaIcon key={index} {...item} />
              ))}
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
