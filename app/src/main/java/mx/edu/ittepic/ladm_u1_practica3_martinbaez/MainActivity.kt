package mx.edu.ittepic.ladm_u1_practica3_martinbaez

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    //var arreglo = ArrayList<Int>(10){0}
    var vector = Array<Int>(10){0}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            //si entra entonces aun no se han otorgados los permisos
            //Código de solicitu
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 0)

        }

        btnAsignar.setOnClickListener {
            insertarArray(valor.text.toString().toInt(), posicion.text.toString().toInt())
        }

        btnMostrar.setOnClickListener {
            mostrar()
        }

        btnGuardar.setOnClickListener {
            guardarSD()
        }

        btnLeer.setOnClickListener {
            leerSD()
            mensaje("Archivo subido correctamente")
        }

    }

    private fun insertarArray(v : Int, p : Int){

        if(valor.text.isEmpty() || posicion.text.isEmpty()){
            mensaje("Los campos de valor y posición no pueden estar vacíos.")
            return
        }
        if(posicion.text.toString().toInt()>9){
            mensaje("La posición del vector no es válida /n [0..9]")
            limpiar()
            return
        }
        vector[p] = v
        limpiar()
        mensaje("Valores inssertado en el vector correctamente")

    }

    private fun mostrar(){
        var ultimoIndice = vector.size-1
        var arregloEstatico : Array<Int> = Array(vector.size,{0})

        (0..ultimoIndice).forEach {
            arregloEstatico[it] = vector[it]
        }

        mensaje("["+arregloEstatico[0]+"]["+arregloEstatico[1]+"]["+arregloEstatico[2]+"]["+arregloEstatico[3]+"]"
                    +"["+arregloEstatico[4]+"]["+arregloEstatico[5]+"]["+arregloEstatico[6]+"]["+arregloEstatico[7]+"]"
                    +"["+arregloEstatico[8]+"]["+arregloEstatico[9]+"]")
    }

    private fun limpiar(){
        valor.setText("")
        posicion.setText("")
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setTitle("Aviso")
            .setMessage(s)
            .setPositiveButton("OK"){d, i->}
            .show()
    }

    private fun guardarSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombreNuevo.text.toString()+".txt")

            var flujoSalida = OutputStreamWriter(
                FileOutputStream(datosArchivo)
            )


            var data = ""

            (0..vector.size-1).forEach {
                data = data + vector[it] + "&"
            }

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("Datos guardados correctamente en la SD")
            limpiar()
        }catch ( error : IOException){
            mensaje(error.message.toString())
        }



    }

    private fun leerSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }


        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombre.text.toString()+".txt")
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()
            var vectorEstatico = data.split("&")

            (0..9).forEach {
                vector[it] = vectorEstatico[it].toString().toInt()
            }

            flujoEntrada.close()
        }catch ( error : IOException ){
            mensaje(error.message.toString())
        }
    }

    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

}
