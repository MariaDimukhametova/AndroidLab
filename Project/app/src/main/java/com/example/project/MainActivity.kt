package com.example.project

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker

    private var pendingIntent: PendingIntent? = null
    private var calendar: Calendar? = null
    private var manager: AlarmManager? = null
    private var service: NotificationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        service = NotificationService(this).also {
            it.showNotification(this)
        }

        fun showTimePicker() {
            picker = MaterialTimePicker.Builder()
                .setTimeFormat(CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText(R.string.time_picker_text)
                .build()

            picker.run {
                show(supportFragmentManager, "Будильник")
                addOnPositiveButtonClickListener {
                    calendar = Calendar.getInstance().also {
                        it[Calendar.HOUR_OF_DAY] = picker.hour
                        it[Calendar.MINUTE] = picker.minute
                        it[Calendar.SECOND] = 0
                        it[Calendar.MILLISECOND] = 0
                    }
                }
            }
        }


        fun saveAlarm(view: View, calendar: Calendar?, manager: AlarmManager?): PendingIntent? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calendar?.timeInMillis.let {
                    val intent = Intent(applicationContext, NotificationReceiver::class.java).apply {
                        action = "com.test.notification_manager"
                    }
                    val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    if (it != null) {
                        manager?.setExact(AlarmManager.RTC_WAKEUP, it, pendingIntent)
                    }
                    Snackbar.make(view, R.string.save_success, 2000).show()
                    return pendingIntent
                }
            }
            return null
        }

        fun setAlarm(){
            if (calendar != null) {
                manager = getSystemService(ALARM_SERVICE) as AlarmManager
                pendingIntent = saveAlarm(binding.root, calendar, manager)
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.set_error,
                    2000)
                    .show()
            }
        }

        fun cancelAlarm(){
            if (calendar != null) {
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(R.string.delete_alarm)
                    setMessage(R.string.delete_alarm_question)
                    setPositiveButton(R.string.ok) {
                            dialog, _ ->
                        manager?.cancel(pendingIntent)
                        manager = null
                        calendar = null
                        dialog.cancel()
                    }
                    setNegativeButton(R.string.no) {
                            dialog, _ -> dialog.cancel()
                    }
                }.show()

            } else {
                Snackbar.make(
                    binding.root,
                    R.string.set_error,
                    2000)
                    .show()
            }
        }


        with(binding) {
            btnSetTime.setOnClickListener {
                showTimePicker()
            }
            btnStart.setOnClickListener {
                setAlarm()
            }
            btnStop.setOnClickListener {
                cancelAlarm()
            }
        }
    }
}