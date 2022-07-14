package com.example.tpueco.presentation.VM

import androidx.lifecycle.ViewModel
import com.example.tpueco.DI.DaggerDocumentFeatureComponent
import com.example.tpueco.DI.DocumentDepsProvider

internal class MailMainViewModel  : ViewModel() {

    val documentFeatureComponent = DaggerDocumentFeatureComponent.builder().deps(
        DocumentDepsProvider.deps).build()


}