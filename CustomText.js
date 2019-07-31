import { NativeModules } from 'react-native'

const CustomText = NativeModules.CustomText;

export default {
   async test(key, content) {
       return await CustomText.test(key, content)
   }
}