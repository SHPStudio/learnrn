import { NativeModules } from 'react-native'

const Calendar = NativeModules.CalendarManage;

export default {
    async getAllAccount() {
        return await Calendar.getAllAccount();
    }
}