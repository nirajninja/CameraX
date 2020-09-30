package com.example.kamera_app

import android.Manifest
import android.media.Image
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    var camera: Camera?=null
    var preview:Preview?=null
    var imageCapture:ImageCapture?=null
   // var cameraSelector:CameraSelector?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PERMISSION_GRANTED)
        {

        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),0)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PERMISSION_GRANTED)
        {
            startCamera()

        }
        else{
            Toast.makeText(this,"Please provide permission",Toast.LENGTH_LONG).show()
        }

        captureBtn.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {

        val photofile= File(externalMediaDirs.firstOrNull(),"kameraAPP -${System.currentTimeMillis()}.jpg")
        val output=ImageCapture.OutputFileOptions.Builder(photofile).build()
        imageCapture?.takePicture(output,ContextCompat.getMainExecutor(this),object:ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Toast.makeText(applicationContext,"SAVED",Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: ImageCaptureException) {

            }

        } )

    }

    private fun startCamera() {
        val cameraProviderFuture=ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
          val cameraProvider=cameraProviderFuture.get()
            preview=Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
           imageCapture=ImageCapture.Builder().build()
            val cameraSelector= CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera=cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
        },ContextCompat.getMainExecutor(this))

    }
}