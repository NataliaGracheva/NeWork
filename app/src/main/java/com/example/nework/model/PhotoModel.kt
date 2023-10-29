package com.example.nework.model

import android.net.Uri
import com.example.nework.enums.AttachmentType
import java.io.InputStream

data class PhotoModel(val uri: Uri? = null, val stream: InputStream? = null, val type: AttachmentType? = null)