import { NativeModules } from 'react-native'

const UserDao = NativeModules.userDao;

export default {
    async insertUserOrUpdate(user) {
        return await UserDao.insertOrUpdateUser(JSON.stringify(user));
    },
    async queryAll() {
        return await UserDao.queryAll();
    }
}
