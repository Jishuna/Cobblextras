package me.jishuna.cobblextras.common

import com.cobblemon.mod.common.api.dialogue.DialogueSpeaker
import com.cobblemon.mod.common.api.dialogue.ReferenceDialogueFaceProvider
import com.cobblemon.mod.common.api.dialogue.WrappedDialogueText
import com.cobblemon.mod.common.entity.npc.NPCEntity

fun NPCEntity.asSpeaker(leftSide: Boolean = true): DialogueSpeaker {
    return DialogueSpeaker(
        WrappedDialogueText(name.copy()),
        ReferenceDialogueFaceProvider(id, leftSide)
    )
}