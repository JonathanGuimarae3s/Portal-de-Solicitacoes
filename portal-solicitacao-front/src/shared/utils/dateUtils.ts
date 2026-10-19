import {format} from "date-fns";

export function formatarLocalDateTimeString(dateString: string): string {
    return format(new Date(dateString), 'dd/MM/yyyy')
}
export function formatarLocalDateTimeParaString(dateString: Date): string {
    return format(new Date(dateString), 'dd/MM/yyyy HH:mm:ss')
}