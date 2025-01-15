<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $request->validate([
            'email' => 'required|email|max:255',
            'name' => 'required|string|max:100',
            'phone_number' => 'required|string|max:15',
            'password' => 'required|string|min:6',
            'role' => 'required|string|max:50',
        ]);

        $user = User::create([
            'email' => $request->email,
            'name' => $request->name,
            'phone_number' => $request->phone_number,
            'password' => Hash::make($request->password),
            'role' => $request->role,
        ]);

        return response()->json([
            'message' => 'Registration successful',
            'data' => $user,
        ], 201);
    }

    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|email|max:255',
            'password' => 'required|string',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json([
                'error' => 'Invalid credentials',
            ], 401);
        }

        return response()->json([
            'message' => 'Login successful',
            'data' => $user,
        ]);
    }

    public function getUsers()
    {
        try {
            // Ambil semua pengguna
            $users = User::all();

            return response()->json($users, 200);
        } catch (\Exception $e) {
            // Tangani error jika terjadi
            return response()->json([
                'error' => 'Failed to retrieve users',
                'message' => $e->getMessage(),
            ], 500);
        }
    }
}
