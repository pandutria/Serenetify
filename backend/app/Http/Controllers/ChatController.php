<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Chat;
use App\Models\User;

class ChatController extends Controller
{
    public function sendMessage(Request $request)
{
    $request->validate([
        'sender_id' => 'required|exists:users,id',
        'receiver_id' => 'required|exists:users,id',
        'message' => 'required|string|max:255',
    ]);

    $sender = User::find($request->sender_id);
    if (!$sender) {
        return response()->json([
            'error' => 'Sender not found',
        ], 404);
    }

    $chat = Chat::create([
        'sender_id' => $request->sender_id,
        'receiver_id' => $request->receiver_id,
        'message' => $request->message,
        'timestamp' => now(),
    ]);

    return response()->json([
        'message' => 'Message sent successfully',
        'data' => $chat,
    ], 201);
}

    public function getChats($receiverId)
{
    $senderId = request()->query('sender_id');

    if (!$senderId) {
        return response()->json([
            'error' => 'Sender ID is required',
        ], 400);
    }

    // Mengambil chat antara pengirim dan penerima
    $chats = Chat::where(function($query) use ($senderId, $receiverId) {
            $query->where('sender_id', $senderId)
                  ->where('receiver_id', $receiverId);
        })
        ->orWhere(function($query) use ($senderId, $receiverId) {
            $query->where('sender_id', $receiverId)
                  ->where('receiver_id', $senderId);
        })
        ->orderBy('timestamp')
        ->get();

    return response()->json( $chats, 200 );
}

public function getChatList(Request $request)
{
    \Log::info('Request parameters:', $request->all());

    $request->validate([
        'user_id' => 'required|exists:users,id',
    ]);

    $userId = $request->input('user_id');

    $chatUsers = Chat::where('sender_id', $userId)
        ->orWhere('receiver_id', $userId)
        ->get()
        ->map(function ($chat) use ($userId) {
            $partnerId = $chat->sender_id == $userId ? $chat->receiver_id : $chat->sender_id;
            return User::find($partnerId);
        })
        ->unique('id') 
        ->values();

    return response()->json($chatUsers, 200);
}



}
