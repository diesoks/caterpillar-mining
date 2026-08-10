import { isAxiosError } from 'axios'

interface BackendErrorBody {
  message?: string
}

// Recovers a human-readable message from any thrown value. When the error comes from the
// backend's GlobalExceptionHandler, its JSON body has a "message" field - this prefers that
// over axios's generic "Request failed with status code 400" text.
export function extractErrorMessage(error: unknown): string {
  if (isAxiosError<BackendErrorBody>(error)) {
    return error.response?.data?.message ?? error.message
  }
  if (error instanceof Error) {
    return error.message
  }
  return 'An unexpected error occurred.'
}
