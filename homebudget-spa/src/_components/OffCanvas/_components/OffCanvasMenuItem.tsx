import styles from "../OffCanvas.module.scss";

interface MenuItemProps {
  icon?: string;
  label: string;
  link?: string;
}

export default function MenuItem({ icon, label, link }: MenuItemProps) {
  return (
    <li className={styles.offcanvas_menu_itens} role="listitem">
      <a href={link} className="d-flex w-100 p-3">
        <span className="br-divider"></span>
        <div className="row align-items-center pl-3">
          <div className="col-auto">
            <i
              className={`${icon} ${styles.offcanvas_menu_itens_icon}`}
              aria-hidden="true"
            ></i>
          </div>
          <div className="col">{label}</div>
        </div>
      </a>
    </li>
  );
}
