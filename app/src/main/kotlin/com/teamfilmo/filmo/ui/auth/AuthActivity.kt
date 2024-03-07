package com.teamfilmo.filmo.ui.auth

import android.content.Intent
import android.net.Uri
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import com.teamfilmo.filmo.util.click
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun init() {
        super.init()
        binding.notice.text =
            buildSpannedString {
                append("계정 생성 시 ")
                click(
                    onClick = {
                        val uri = Uri.parse("https://www.google.com")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    },
                    color = this@AuthActivity.getColor(R.color.grey),
                ) {
                    bold {
                        append("개인정보수집방침 및 이용약관")
                    }
                }
                append('\n')
                append("(마케팅 정보 수신 동의 포함)에 동의하게 됩니다.")
            }
    }
}