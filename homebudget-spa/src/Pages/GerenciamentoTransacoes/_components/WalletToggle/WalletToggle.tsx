import { PiWalletLight } from "react-icons/pi";
import { LiaWalletSolid } from "react-icons/lia";
import { motion, AnimatePresence } from "framer-motion";
import styles from "./WalletToggle.module.scss";

interface Props {
  openWallet: boolean;
}

export default function WalletToggle({ openWallet }: Props) {
  return (
    <div className={styles.wallet_container}>
      <AnimatePresence mode="wait">
        {openWallet ? (
          <motion.div
            key="solid"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.8 }}
            transition={{ duration: 0.2 }}
          >
            <LiaWalletSolid size={35} />
          </motion.div>
        ) : (
          <motion.div
            key="light"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.8 }}
            transition={{ duration: 0.2 }}
          >
            <PiWalletLight size={35} />
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
