import type { ButtonHTMLAttributes } from 'react'
import styles from './Button.module.css'

type ButtonVariant = 'primary' | 'secondary' | 'danger'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant
}

// Small, reusable button that standardizes the app's three visual variants.
export function Button({ variant = 'primary', className, ...buttonProps }: ButtonProps) {
  const variantClassName = styles[variant]
  const combinedClassName = [styles.button, variantClassName, className].filter(Boolean).join(' ')

  return <button className={combinedClassName} {...buttonProps} />
}
