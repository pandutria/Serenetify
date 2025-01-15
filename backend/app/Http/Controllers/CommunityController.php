<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Community;
use App\Models\CommunityMessage;

class CommunityController extends Controller
{
    public function getCommunities()
    {
        $communities = Community::all();
        return response()->json($communities, 200);
    }

    public function getCommunity($id)
    {
        $community = Community::find($id);

        if (!$community) {
            return response()->json(['error' => 'Community not found'], 404);
        }

        return response()->json($community, 200);
    }

    public function createCommunity(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'nullable|string',
            'image_url' => 'nullable|string|max:255',
            'logo_url' => 'nullable|string|max:255',
        ]);

        $community = Community::create($request->all());

        return response()->json([
            'message' => 'Community created successfully',
            'data' => $community,
        ], 201);
    }

    public function deleteCommunity($id)
    {
        $community = Community::find($id);

        if (!$community) {
            return response()->json(['error' => 'Community not found'], 404);
        }

        $community->delete();

        return response()->json(['message' => 'Community deleted successfully'], 200);
    }

    public function updateCommunity(Request $request, $id)
    {
    $request->validate([
        'name' => 'required|string|max:255',
        'description' => 'nullable|string',
        'image_url' => 'nullable|string|max:255',
        'logo_url' => 'nullable|string|max:255',
    ]);

    $community = Community::find($id);

    if (!$community) {
        return response()->json(['error' => 'Community not found'], 404);
    }

    $community->update($request->all());

    return response()->json([
        'message' => 'Community updated successfully',
        'data' => $community,
    ], 200);
    }

}
