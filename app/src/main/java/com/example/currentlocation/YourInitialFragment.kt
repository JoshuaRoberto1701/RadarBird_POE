import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.currentlocation.MainActivity
import com.example.currentlocation.R
import com.example.currentlocation.SettingsFragment

class YourInitialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_your_initial, container, false)

        // Handle the settings icon click as you've done before
        val settingsIcon: ImageView = view.findViewById(R.id.settings_icon)
        settingsIcon.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SettingsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Handle the map icon click to open MainActivity
        val mapIcon: ImageView = view.findViewById(R.id.mapIcon)  // Assuming this is the map icon ID
        mapIcon.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
