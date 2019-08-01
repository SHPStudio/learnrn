import { NativeModules } from 'react-native'

const Calendar = NativeModules.CalendarModule;

export default {
    async getAllAccount() {
        return await Calendar.getAllAccount();
    },
    async addAccount(name, email) {
        return await Calendar.addAccount(name,email)
    },
    async addCalendarEvent(message) {
        return await Calendar.addCalendarEvent(message);
    }
}