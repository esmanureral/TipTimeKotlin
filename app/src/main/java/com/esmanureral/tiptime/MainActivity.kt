package com.esmanureral.tiptime

import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esmanureral.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import androidx.compose.material3.Switch
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(// Uygulamanın genel yüzeyini tanımlar
                    modifier = Modifier.fillMaxSize() // Ekranın tamamını kaplar
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 15.0 // Varsayılan olarak %15
    var roundUp by remember { mutableStateOf(false) }
    val tip = calculateTip(amount, tipPercent, roundUp)


    Column(
        modifier = Modifier
            .padding(30.dp)
            .verticalScroll(rememberScrollState()), // İçeriğin kaydırılabilir olmasını sağlar
        horizontalAlignment = Alignment.CenterHorizontally, // Yatayda ortalama
        verticalArrangement = Arrangement.Center // Dikeyde ortalama
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )

        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            value = amountInput,
            onValueChanged = { amountInput = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percentage,
            value = tipInput,
            onValueChanged = { tipInput = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(
            roundUp = roundUp, // Kullanıcının seçtiği durum: açma/kapama düğmesinin durumu
            onRoundUpChanged = { roundUp = it }, // Düğmenin durumu değiştiğinde çağrılacak fonksiyon
            modifier = Modifier.padding(bottom = 32.dp) // Aşağıya 32 dp boşluk bırakmak için modifier
        )

        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = stringResource(label), // Erişilebilirlik için açıklama
                modifier = Modifier.size(24.dp) // İkon boyutunu 24 dp olarak ayarlayın
            )
        },
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}


/**
 * Bahşişi kullanıcı girdisine göre hesaplar ve bahşiş miktarını
 * yerel para birimine göre biçimlendirir.
 * Örnek "$10.00" olur.
 */

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount // Bahşiş miktarını hesapla
    if (roundUp) { // Eğer roundUp true ise
        tip = kotlin.math.ceil(tip) // Bahşişi yukarı yuvarla
    }
    return NumberFormat.getCurrencyInstance().format(tip) // Bahşiş miktarını yerel para birimi formatında döndür
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {


    TipTimeTheme {
        TipTimeLayout()
    }
}
//Switch
@Composable
fun RoundTheTipRow(
    roundUp:Boolean,
    onRoundUpChanged:(Boolean)->Unit,
    modifier:Modifier=Modifier
)
{
    Row(
        modifier = modifier
            .fillMaxWidth() // Satırı ekranın tamamına yayar.
            .size(48.dp),   // Satırın boyutunu 48dp olarak ayarlar.
        verticalAlignment = Alignment.CenterVertically, // Bileşenleri dikeyde ortalar.
    ) {
        Text(text = stringResource(R.string.round_up_tip)) // 'round_up_tip' string kaynağını ekrana yazar.
    }
    Switch(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End),//bileşen ekran sonuna
        checked = roundUp,  // 'roundUp' değeriyle anahtarın açık veya kapalı olduğunu ayarlar.
        onCheckedChange = onRoundUpChanged //anahtar tıklandığında çağrılacak geri çağırma
    )
}




