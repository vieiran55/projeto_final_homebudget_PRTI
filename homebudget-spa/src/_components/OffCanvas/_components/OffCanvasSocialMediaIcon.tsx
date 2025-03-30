import { Link } from "react-router-dom";

interface SocialMediaIconProps {
  icon: string;
  nome: string;
  link: string;
}

export default function SocialMediaIcon({
  icon,
  nome,
  link,
}: SocialMediaIconProps) {
  return (
    <Link to={link} target="_blank" aria-label={nome} className="py-1">
      <i className={icon}></i>
    </Link>
  );
}
