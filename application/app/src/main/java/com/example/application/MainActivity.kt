package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.application.databinding.ActivityMainBinding

private const val TAG_FIRST = "TAG_FIRST"

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .add(R.id.fragment_container_view_tag, FirstFragment(), TAG_FIRST)
            .commit()

        with(binding) {
            btnFirst.setOnClickListener{
                replaceFragment(FirstFragment())
            }

            btnSecond.setOnClickListener{
                replaceFragment(SecondFragment())
            }

            btnThird.setOnClickListener{
                replaceFragment(ThirdFragment())
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.fragment_container_view_tag, fragment)
            .addToBackStack(null)
            .commit()
    }

}


