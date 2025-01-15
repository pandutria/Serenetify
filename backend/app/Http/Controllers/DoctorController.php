<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Doctor;
use App\Models\User;

class DoctorController extends Controller
{
    public function createDoctor(Request $request)
    {
        $request->validate([
            'image_url' => 'nullable|url',
            'user_id' => 'required|exists:users,id',
        ]);

        $doctor = Doctor::create($request->all());

        return response()->json([
            'message' => 'Doctor created successfully',
            'data' => $doctor,
        ], 201);
    }

    public function getDoctors()
    {
        $doctors = Doctor::with('user')->get();
        return response()->json($doctors, 200);
    }

    public function getDoctor($id)
    {
        $doctor = Doctor::with('user')->find($id);

        if (!$doctor) {
            return response()->json(['error' => 'Doctor not found'], 404);
        }

        return response()->json($doctor, 200);
    }

    public function deleteDoctor($id)
    {
        $doctor = Doctor::find($id);

        if (!$doctor) {
            return response()->json(['error' => 'Doctor not found'], 404);
        }

        $doctor->delete();

        return response()->json(['message' => 'Doctor deleted successfully'], 200);
    }

    public function updateDoctor(Request $request, $id)
{
    $request->validate([
        'image_url' => 'nullable|url',
        'user_id' => 'required|exists:users,id',
    ]);

    $doctor = Doctor::find($id);

    if (!$doctor) {
        return response()->json(['error' => 'Doctor not found'], 404);
    }

    $doctor->update($request->all());

    return response()->json([
        'message' => 'Doctor updated successfully',
        'data' => $doctor,
    ], 200);
}

}
