/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 * @lint-ignore-every XPLATJSCOPYRIGHT1
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Button, Alert, TimePickerAndroid,DatePickerAndroid} from 'react-native';
import CustomText from './CustomText'
import Calendar from './Calendar'

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
    android:
        'Double tap R on your keyboard to reload,\n' +
        'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
    constructor(props) {
        super(props);
        this.state = {
            targetText: '',
            targetHour: null,
            targetMinute: null,
            targetYear: null,
            targetMonth: null,
            targetDay: null
        }
    }

    onPressTime= async() =>{
        try {
            let date = new Date();
            const {action, hour, minute} = await TimePickerAndroid.open({
                hour: date.getHours(),
                minute: date.getMinutes(),
                is24Hour: false, // Will display '2 PM'
            });
            if (action !== TimePickerAndroid.dismissedAction) {
                // Selected hour (0-23), minute (0-59)
                this.setState({targetHour: hour, targetMinute: minute})
            }
        } catch ({code, message}) {
            console.warn('Cannot open time picker', message);
        }
    };

    onPressDate= async () => {
        try {
            const {action, year, month, day} = await DatePickerAndroid.open({
                // 要设置默认值为今天的话，使用`new Date()`即可。
                // 下面显示的会是2020年5月25日。月份是从0开始算的。
                date: new Date()
            });
            if (action !== DatePickerAndroid.dismissedAction) {
                // 这里开始可以处理用户选好的年月日三个参数：year, month (0-11), day
                this.setState({targetYear: year, targetMonth: month + 1, targetDay: day})
            }
        } catch ({code, message}) {
            console.warn('Cannot open date picker', message);
        }
    };
    onPressAddEvent= async () => {
        let date = new Date(this.state.targetYear, this.state.targetMonth -1, this.state.targetDay, this.state.targetHour, this.state.targetMinute);
        let endDate = new Date(this.state.targetYear, this.state.targetMonth -1, this.state.targetDay, this.state.targetHour, this.state.targetMinute + 1)
        let request = {
            title: "测试事件",
            description: "测试事件",
            startTime: date.getTime(),
            endTime: endDate.getTime()
        }
        const result = await Calendar.addCalendarEvent(JSON.stringify(request));
        if (result == 0) {
            Alert.alert("添加成功")
        }else {
            Alert.alert("添加失败")
        }
    };

    onPressDeleteOutDateEvent= async () => {
        const result = await Calendar.deleteOutDateEvent();
        if (result) {
            Alert.alert("删除成功");
        }
    }



    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}>Welcome to React Native!</Text>
                <Text style={styles.instructions}>To get started, edit App.js</Text>
                <Text style={styles.instructions}>{instructions}</Text>
                <Text style={styles.instructions}>选择的时间: {this.state.targetYear}-{this.state.targetMonth}-{this.state.targetDay} {this.state.targetHour}:{this.state.targetMinute}</Text>
                <Button title="调用日期选择" onPress={this.onPressDate}/>
                <Button title="调用时间选择" onPress={this.onPressTime}/>
                <Button title="增加事件" onPress={this.onPressAddEvent}/>
                <Button title="删除过时事件" onPress={this.onPressDeleteOutDateEvent}/>
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
