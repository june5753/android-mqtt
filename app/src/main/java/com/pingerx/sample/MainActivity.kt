package com.pingerx.sample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pingerx.mqtt.MqttConfig
import com.pingerx.mqtt.MqttManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //AndroidTopic  AndroidPublishTopic
    private val subscriptionTopic = "AndroidTopic"
    private val publishTopic = subscriptionTopic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 初始化
        MqttManager.getInstance().init(this, MqttConfig().create())

        showTips("服务器地址：${MqttManager.getInstance().getServerUrl()}")

        setListener()
    }

    private fun setListener() {
        // 连接服务端
        btnConnect.setOnClickListener {
            Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show()

            showTips("正在连接中...")
            MqttManager.getInstance().connect {
                onConnectSuccess {
                    showTips("服务器连接成功")

                }
                onConnectFailed {
                    showTips("服务器连接失败：${it?.message}")
                }
            }
        }
        // 订阅主题
        btnSubscribe.setOnClickListener {
            showTips("正在订阅中...")
            MqttManager.getInstance().subscribe(subscriptionTopic) {

                onSubscriberSuccess {
                    showTips("订阅成功")
                }

                onSubscriberFailed {
                    showTips("订阅失败：${it?.message}")
                }

                onDeliveryComplete {
                    showTips("消息推送完毕：$it")
                }

                onConnectionLost {
                    showTips("连接已断开")
                }
                onDeliveryComplete {
//                    showTips("收到消息：$it")
                }            }
        }

        // 推送消息
        btnPublish.setOnClickListener {
            showTips("正在推送中...")
            MqttManager.getInstance().publishMessage(publishTopic, "Hello Mqtt...")
        }

        // 断开连接
        btnClose.setOnClickListener {
            showTips("正在断开中...")
            MqttManager.getInstance().disconnect()
        }
    }

    private fun showTips(msg: String?) {
        tvMessage?.text = msg
    }


    override fun onDestroy() {
        super.onDestroy()
        MqttManager.getInstance().close()
    }
}
