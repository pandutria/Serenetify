<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\CommunityMessage;


class CommunityMessageController extends Controller
{
    public function getMessages($communityId)
    {
        $messages = CommunityMessage::where('community_id', $communityId)->with('sender')->get();
        return response()->json($messages, 200);
    }

    public function createMessage(Request $request)
    {
        $request->validate([
            'community_id' => 'required|exists:communities,id',
            'sender_id' => 'required|exists:users,id',
            'message' => 'required|string',
        ]);

        $message = CommunityMessage::create($request->all());

        return response()->json([
            'message' => 'Message created successfully',
            'data' => $message,
        ], 201);
    }
}
